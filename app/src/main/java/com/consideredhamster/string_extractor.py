import os
import sys
import json
import javalang
import re
from collections import defaultdict

# Constants
MAX_FILES = 2000
STRING_METHOD = "Ml.g"
OUTPUT_JSON = "strings.json"
OUTPUT_SNIPPETS = "extracted_strings.txt"
JAVA_FILE_EXTENSIONS = ['.java']
REDUNDANT_PACKAGE_PREFIX = 'com.consideredhamster.yetanotherpixeldungeon.'

# Globals
file_count = 0
strings_dict = {}
snippets = []
processed_files = []
string_id_counts = defaultdict(int)
modified_files = set()

def process_java_file(file_path):
    global file_count
    if file_count >= MAX_FILES:
        print(f"Maximum file limit of {MAX_FILES} reached. Stopping processing.")
        return

    file_count += 1
    print(f"Processing file {file_count}: {file_path}")

    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            code = f.read()

        tree = javalang.parse.parse(code)
        package = get_package_name(tree)
        class_name = get_class_name(tree)

        # Read code lines for replacements
        code_lines = code.split('\n')

        # Process field declarations (variables outside methods)
        for path, node in tree.filter(javalang.tree.FieldDeclaration):
            for declarator in node.declarators:
                if declarator.initializer and is_string_expression(declarator.initializer):
                    string_info = extract_string(declarator.initializer)
                    if string_info:
                        # Skip empty or whitespace-only strings
                        if string_info['string'].strip() == '':
                            continue
                        var_name = declarator.name
                        string_id = generate_string_id(package, class_name, var_name)
                        strings_dict[string_id] = string_info['string']
                        args = ', '.join(string_info['variables'])
                        # Build the replacement initializer
                        initializer_replacement = f"{STRING_METHOD}(\"{string_id}\"{', ' + args if args else ''})"
                        # Replace the initializer in the code
                        replace_variable_initializer(code_lines, node, declarator, initializer_replacement)
                        snippets.append(f"Replaced field declaration of {var_name} with {STRING_METHOD}(\"{string_id}\"...)")
                        modified_files.add(file_path)

        # Process methods
        for path, node in tree.filter(javalang.tree.MethodDeclaration):
            method_name = node.name
            method_start = node.position.line - 1 if node.position else 0
            method_end = get_method_end_line(node)

            # Handle return statements with concatenated strings
            for path2, node2 in node.filter(javalang.tree.ReturnStatement):
                if node2.expression:
                    # Check if the return expression is a string
                    if is_string_expression(node2.expression):
                        string_info = extract_string(node2.expression)
                        if string_info:
                            # Skip empty or whitespace-only strings
                            if string_info['string'].strip() == '':
                                continue
                            string_id = generate_string_id(package, class_name, method_name)
                            strings_dict[string_id] = string_info['string']
                            args = ', '.join(string_info['variables'])
                            replacement = f"return {STRING_METHOD}(\"{string_id}\"{', ' + args if args else ''});"
                            # Replace the full return statement
                            replace_full_statement(code_lines, node2, replacement)
                            snippets.append(f"Replaced return statement in {method_name} with {replacement}")
                            modified_files.add(file_path)
                    else:
                        # Skip non-string return expressions
                        continue

            # Handle variable initializations within methods
            for path2, node2 in node.filter(javalang.tree.LocalVariableDeclaration):
                for declarator in node2.declarators:
                    if declarator.initializer and is_string_expression(declarator.initializer):
                        string_info = extract_string(declarator.initializer)
                        if string_info:
                            # Skip empty or whitespace-only strings
                            if string_info['string'].strip() == '':
                                continue
                            var_name = declarator.name
                            string_id = generate_string_id(package, class_name, var_name)
                            strings_dict[string_id] = string_info['string']
                            args = ', '.join(string_info['variables'])
                            # Build the replacement initializer
                            initializer_replacement = f"{STRING_METHOD}(\"{string_id}\"{', ' + args if args else ''})"
                            # Replace the initializer in the code
                            replace_variable_initializer(code_lines, node2, declarator, initializer_replacement)
                            snippets.append(f"Replaced variable initialization of {var_name} with {STRING_METHOD}(\"{string_id}\"...)")
                            modified_files.add(file_path)
                        else:
                            continue

            # Handle expressions like return statements in switch cases
            for path2, node2 in node.filter(javalang.tree.SwitchStatementCase):
                for stmt in node2.statements:
                    if isinstance(stmt, javalang.tree.BlockStatement):
                        for sub_stmt in stmt.statements:
                            if isinstance(sub_stmt, javalang.tree.ReturnStatement) and sub_stmt.expression:
                                if is_string_expression(sub_stmt.expression):
                                    string_info = extract_string(sub_stmt.expression)
                                    if string_info:
                                        # Skip empty or whitespace-only strings
                                        if string_info['string'].strip() == '':
                                            continue
                                        string_id = generate_string_id(package, class_name, method_name)
                                        strings_dict[string_id] = string_info['string']
                                        args = ', '.join(string_info['variables'])
                                        replacement = f"return {STRING_METHOD}(\"{string_id}\"{', ' + args if args else ''});"
                                        # Replace the full return statement
                                        replace_full_statement(code_lines, sub_stmt, replacement)
                                        snippets.append(f"Replaced return statement in switch case in {method_name} with {replacement}")
                                        modified_files.add(file_path)

        # Add import statement if the file was modified
        if file_path in modified_files:
            code_lines = add_import_statement(code_lines)

            # Write the modified code back to the file
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write('\n'.join(code_lines))

        processed_files.append(file_path)

    except Exception as e:
        print(f"Error processing {file_path}: {e}")

def is_string_expression(node):
    """
    Determines if the expression node represents a string or string concatenation.
    """
    if isinstance(node, javalang.tree.Literal):
        value = node.value.strip('"').strip()
        return node.value.startswith('"') and node.value.endswith('"') and value != ''
    elif isinstance(node, javalang.tree.BinaryOperation) and node.operator == '+':
        return is_string_expression(node.operandl) or is_string_expression(node.operandr)
    elif isinstance(node, javalang.tree.TernaryExpression):
        return is_string_expression(node.if_true) or is_string_expression(node.if_false)
    else:
        return False  # For other types, we assume it's not a string

def extract_string(node, index=0):
    """
    Recursively extracts the string and variables from an expression.
    """
    if isinstance(node, javalang.tree.Literal):
        if node.value.startswith('"') and node.value.endswith('"'):
            string_value = node.value.strip('"').replace('\\"', '"')
            if string_value.strip() == '':
                return None  # Skip empty or whitespace-only strings
            return {'string': string_value, 'variables': [], 'index': index}
        else:
            return None
    elif isinstance(node, javalang.tree.BinaryOperation) and node.operator == '+':
        left = extract_string(node.operandl, index)
        if left:
            index += len(left['variables'])
        right = extract_string(node.operandr, index)
        if left and right:
            combined_string = left['string'] + right['string']
            variables = left['variables'] + right['variables']
            return {'string': combined_string, 'variables': variables, 'index': index}
        elif left:
            right_var = extract_variable(node.operandr)
            combined_string = left['string'] + f"{{{len(left['variables'])}}}"
            variables = left['variables'] + [right_var]
            return {'string': combined_string, 'variables': variables, 'index': index}
        elif right:
            left_var = extract_variable(node.operandl)
            combined_string = f"{{{len(right['variables'])}}}" + right['string']
            variables = [left_var] + right['variables']
            return {'string': combined_string, 'variables': variables, 'index': index}
        else:
            left_var = extract_variable(node.operandl)
            right_var = extract_variable(node.operandr)
            combined_string = f"{{{index}}}{{{index+1}}}"
            variables = [left_var, right_var]
            return {'string': combined_string, 'variables': variables, 'index': index}
    elif isinstance(node, javalang.tree.TernaryExpression):
        # Handle ternary expressions separately
        true_part = extract_string(node.if_true, index)
        false_part = extract_string(node.if_false, index)
        condition_expr = extract_expression(node.condition)
        if true_part and false_part:
            # Both sides are strings
            combined_string = f"{{{index}}}"
            variables = [f"({condition_expr} ? {true_part['string']} : {false_part['string']})"]
            return {'string': combined_string, 'variables': variables, 'index': index}
        else:
            # One or both sides are variables
            true_var = extract_variable(node.if_true)
            false_var = extract_variable(node.if_false)
            ternary_var = f"({condition_expr} ? {true_var} : {false_var})"
            return {'string': f"{{{index}}}", 'variables': [ternary_var], 'index': index}
    else:
        # Not a string expression
        return None

def extract_variable(node):
    if isinstance(node, javalang.tree.MemberReference):
        return node.member
    elif isinstance(node, javalang.tree.MethodInvocation):
        return generate_method_call_code(node)
    elif isinstance(node, javalang.tree.Literal):
        return node.value
    elif isinstance(node, javalang.tree.Cast):
        return extract_variable(node.expression)
    elif isinstance(node, javalang.tree.TernaryExpression):
        # For ternary expressions, construct the ternary expression
        condition = extract_expression(node.condition)
        true_var = extract_variable(node.if_true)
        false_var = extract_variable(node.if_false)
        return f"({condition} ? {true_var} : {false_var})"
    else:
        return extract_expression(node)

def extract_expression(node):
    if isinstance(node, javalang.tree.MemberReference):
        return node.member
    elif isinstance(node, javalang.tree.MethodInvocation):
        return generate_method_call_code(node)
    elif isinstance(node, javalang.tree.Literal):
        return node.value
    elif isinstance(node, javalang.tree.BinaryOperation):
        left = extract_expression(node.operandl)
        right = extract_expression(node.operandr)
        return f"{left} {node.operator} {right}"
    elif isinstance(node, javalang.tree.Cast):
        return extract_expression(node.expression)
    elif isinstance(node, javalang.tree.TernaryExpression):
        condition = extract_expression(node.condition)
        true_expr = extract_expression(node.if_true)
        false_expr = extract_expression(node.if_false)
        return f"({condition} ? {true_expr} : {false_expr})"
    else:
        return "UNKNOWN_EXPRESSION"

def generate_method_call_code(node):
    args = ', '.join([extract_expression(arg) for arg in node.arguments])
    return f"{node.member}({args})"

def replace_full_statement(code_lines, node, replacement):
    if node.position:
        start_line = node.position.line - 1
        # Find the end of the statement
        end_line = find_statement_end_line(code_lines, start_line)
        # Replace the lines from start_line to end_line with the replacement
        code_lines[start_line:end_line + 1] = [replacement]
    else:
        print(f"Node position is None for node")
        return

def replace_variable_initializer(code_lines, parent_node, declarator_node, replacement):
    # Use parent_node for position
    if parent_node.position:
        start_line = parent_node.position.line - 1
    else:
        print(f"Node position is None for variable {declarator_node.name}")
        return
    end_line = find_statement_end_line(code_lines, start_line)
    # Get the code for the variable declaration
    declaration_lines = code_lines[start_line:end_line + 1]
    declaration_code = '\n'.join(declaration_lines)
    var_name = declarator_node.name
    # Check if there are multiple variables declared in the same statement
    if len(parent_node.declarators) == 1:
        # Single-variable declaration
        # Match from variable name and equals sign to semicolon
        pattern = re.compile(
            r'(\b' + re.escape(var_name) + r'\b\s*=\s*)(.*?)(;)', re.DOTALL)
        match = pattern.search(declaration_code)
        if match:
            new_var_declaration = match.group(1) + replacement + match.group(3)
            declaration_code = declaration_code[:match.start()] + new_var_declaration + declaration_code[match.end():]
            new_declaration_lines = declaration_code.split('\n')
            code_lines[start_line:end_line + 1] = new_declaration_lines
        else:
            print(f"Could not find variable {var_name} in code")
    else:
        # Multiple variables declared in the same statement
        # Need to match up to comma or semicolon
        pattern = re.compile(
            r'(\b' + re.escape(var_name) + r'\b\s*=\s*)(.*?)([,;])', re.DOTALL)
        match = pattern.search(declaration_code)
        if match:
            new_var_declaration = match.group(1) + replacement + match.group(3)
            declaration_code = declaration_code[:match.start()] + new_var_declaration + declaration_code[match.end():]
            new_declaration_lines = declaration_code.split('\n')
            code_lines[start_line:end_line + 1] = new_declaration_lines
        else:
            print(f"Could not find variable {var_name} in code")

def find_statement_end_line(code_lines, start_line):
    """
    Finds the end line of a statement starting from start_line by searching for the terminating semicolon.
    """
    open_brackets = 0
    for line_num in range(start_line, len(code_lines)):
        line = code_lines[line_num]
        # Remove comments
        line = re.sub(r'//.*', '', line)
        line = re.sub(r'/\*.*?\*/', '', line)
        # Count brackets to handle nested statements
        open_brackets += line.count('(') - line.count(')')
        if ';' in line and open_brackets <= 0:
            return line_num
    return start_line  # Default to start_line if no semicolon is found

def add_import_statement(code_lines):
    """
    Adds the import statement after the package declaration.
    """
    for i, line in enumerate(code_lines):
        if line.strip().startswith('package '):
            # Find the next non-empty line
            j = i + 1
            while j < len(code_lines) and code_lines[j].strip() == '':
                j += 1
            # Check if the import statement already exists
            import_line = 'import com.consideredhamster.yetanotherpixeldungeon.multilang.Ml;'
            if import_line not in code_lines:
                # Insert the import statement
                code_lines.insert(j, import_line)
            break
    return code_lines

def get_package_name(tree):
    if tree.package:
        return tree.package.name
    return ""

def get_class_name(tree):
    for path, node in tree.filter(javalang.tree.ClassDeclaration):
        return node.name
    return "UnknownClass"

def get_method_end_line(method_node):
    """
    Estimates the end line of a method based on its body.
    """
    if method_node.body and method_node.body[-1].position:
        return method_node.body[-1].position.line
    else:
        return method_node.position.line if method_node.position else 0

def generate_string_id(package, class_name, name, suffix=None):
    # Remove redundant package prefix
    package = package.lower()
    if package.startswith(REDUNDANT_PACKAGE_PREFIX):
        package = package[len(REDUNDANT_PACKAGE_PREFIX):]
    class_name = class_name.lower()
    name = name.lower()
    string_id_base = f"{package}.{class_name}.{name}"
    if suffix:
        string_id_base += f"_{suffix}"
    # Ensure uniqueness
    string_id_counts[string_id_base] += 1
    count = string_id_counts[string_id_base]
    if count > 1:
        string_id = f"{string_id_base}_{count}"
    else:
        string_id = string_id_base
    return string_id

def scan_directory(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if any(file.endswith(ext) for ext in JAVA_FILE_EXTENSIONS):
                file_path = os.path.join(root, file)
                process_java_file(file_path)
                if file_count >= MAX_FILES:
                    return

def write_output():
    # Write JSON file
    with open(OUTPUT_JSON, 'w', encoding='utf-8') as f:
        json.dump(strings_dict, f, ensure_ascii=False, indent=4)

    # Write code snippets
    with open(OUTPUT_SNIPPETS, 'w', encoding='utf-8') as f:
        for snippet in snippets:
            f.write(snippet + '\n\n')

    # Write processed files list
    with open('processed_files.txt', 'w', encoding='utf-8') as f:
        for file_path in processed_files:
            f.write(file_path + '\n')

def main():
    current_directory = os.getcwd()
    print(f"Starting scan in directory: {current_directory}")
    scan_directory(current_directory)
    write_output()
    print("Processing complete.")

if __name__ == "__main__":
    main()
