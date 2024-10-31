import os
import json
import javalang
import re
from collections import defaultdict

# Constants
MAX_FILES = 2000
STRING_METHOD = "Ml.g"
OUTPUT_JSON = "name_info_strings.json"
JAVA_FILE_EXTENSIONS = ['.java']
REDUNDANT_PACKAGE_PREFIX = 'com.consideredhamster.yetanotherpixeldungeon.'

# Globals
file_count = 0
strings_dict = {}
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

        # Process constructors
        for path, node in tree.filter(javalang.tree.ConstructorDeclaration):
            process_assignments_in_block(node.body, code_lines, package, class_name, file_path)

        # Process class body for initializer blocks
        for path, node in tree.filter(javalang.tree.ClassDeclaration):
            print(f"Processing ClassDeclaration: {node.name}")
            for body_decl in node.body:
                if isinstance(body_decl, list):
                    for elem in body_decl:
                        process_class_body_decl(elem, code_lines, package, class_name, file_path)
                else:
                    process_class_body_decl(body_decl, code_lines, package, class_name, file_path)

        # Add import statement if the file was modified
        if file_path in modified_files:
            code_lines = add_import_statement(code_lines)

            # Write the modified code back to the file
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write('\n'.join(code_lines))

        processed_files.append(file_path)

    except Exception as e:
        print(f"Error processing {file_path}: {e}")

def process_class_body_decl(body_decl, code_lines, package, class_name, file_path):
    if isinstance(body_decl, javalang.tree.MethodDeclaration):
        # Skip methods
        return
    elif isinstance(body_decl, javalang.tree.ConstructorDeclaration):
        # Skip constructors (already processed)
        return
    elif isinstance(body_decl, javalang.tree.FieldDeclaration):
        # Skip fields
        return
    elif isinstance(body_decl, javalang.tree.ClassDeclaration):
        # Inner class, can process recursively if needed
        return
    elif isinstance(body_decl, javalang.tree.BlockStatement):
        # Process BlockStatement nodes
        process_assignments_in_block(body_decl, code_lines, package, class_name, file_path)
    elif isinstance(body_decl, javalang.tree.StatementExpression):
        # Process StatementExpression directly
        process_assignments_in_block(body_decl, code_lines, package, class_name, file_path)
    else:
        # For unknown node types, check if they have 'statements' or 'body' attributes
        if hasattr(body_decl, 'statements'):
            process_assignments_in_block(body_decl, code_lines, package, class_name, file_path)
        elif hasattr(body_decl, 'body'):
            process_assignments_in_block(body_decl.body, code_lines, package, class_name, file_path)
        else:
            # Print the type for debugging
            print(f"Unknown body_decl type: {type(body_decl)}")

def process_assignments_in_block(block, code_lines, package, class_name, file_path):
    if block is None:
        return
    if isinstance(block, list):
        statements = block
    elif isinstance(block, javalang.tree.BlockStatement):
        statements = block.statements
    elif isinstance(block, javalang.tree.StatementExpression):
        statements = [block]
    elif hasattr(block, 'statements') and block.statements is not None:
        statements = block.statements
    else:
        # If block is a single statement, process it directly
        statements = [block]

    for stmt in statements:
        if isinstance(stmt, javalang.tree.StatementExpression):
            expr = stmt.expression
            if isinstance(expr, javalang.tree.Assignment):
                # Check if the left-hand side is 'name' or 'info'
                if isinstance(expr.expressionl, javalang.tree.MemberReference):
                    if expr.expressionl.member in ('name', 'info'):
                        field_name = expr.expressionl.member
                        # Check if the right-hand side is a string literal
                        if is_string_expression(expr.value):
                            string_info = extract_string(expr.value)
                            if string_info:
                                if string_info['string'].strip() == '':
                                    continue
                                string_id = generate_string_id(package, class_name, field_name)
                                strings_dict[string_id] = string_info['string']
                                args = ', '.join(string_info['variables'])
                                replacement = f"{field_name} = {STRING_METHOD}(\"{string_id}\"{', ' + args if args else ''});"
                                replace_assignment_in_code(code_lines, stmt, replacement)
                                modified_files.add(file_path)
        elif isinstance(stmt, javalang.tree.BlockStatement):
            # Recursively process nested blocks
            process_assignments_in_block(stmt, code_lines, package, class_name, file_path)
        elif isinstance(stmt, javalang.tree.IfStatement):
            # Handle if-else statements
            process_assignments_in_block(stmt.then_statement, code_lines, package, class_name, file_path)
            if stmt.else_statement:
                process_assignments_in_block(stmt.else_statement, code_lines, package, class_name, file_path)
        elif isinstance(stmt, (javalang.tree.WhileStatement, javalang.tree.ForStatement, javalang.tree.DoStatement)):
            # Handle loops
            process_assignments_in_block(stmt.body, code_lines, package, class_name, file_path)
        elif isinstance(stmt, javalang.tree.TryStatement):
            # Handle try-catch blocks
            process_assignments_in_block(stmt.block, code_lines, package, class_name, file_path)
            for catch_clause in stmt.catches:
                process_assignments_in_block(catch_clause.block, code_lines, package, class_name, file_path)
            if stmt.finally_block:
                process_assignments_in_block(stmt.finally_block, code_lines, package, class_name, file_path)
        else:
            # Print unknown statement types for debugging
            print(f"Unknown statement type: {type(stmt)}")
            # You can add more cases as needed for other statement types

def replace_assignment_in_code(code_lines, node, replacement):
    if node.position:
        start_line = node.position.line - 1
        end_line = get_node_end_line(node) - 1
        # Replace the lines from start_line to end_line with the replacement
        code_lines[start_line:end_line + 1] = [replacement]
    else:
        print(f"Node position is None for assignment")
        return

def is_string_expression(node):
    """
    Determines if the expression node represents a string or string concatenation.
    """
    if isinstance(node, javalang.tree.Literal):
        value = node.value.strip('"').strip()
        return node.value.startswith('"') and node.value.endswith('"') and value != ''
    elif isinstance(node, javalang.tree.BinaryOperation) and node.operator == '+':
        return is_string_expression(node.operandl) or is_string_expression(node.operandr)
    elif isinstance(node, javalang.tree.Cast):
        return is_string_expression(node.expression)
    else:
        return False

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
    elif isinstance(node, javalang.tree.Cast):
        return extract_string(node.expression, index)
    else:
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
    else:
        return "UNKNOWN_EXPRESSION"

def generate_method_call_code(node):
    args = ', '.join([extract_expression(arg) for arg in node.arguments])
    return f"{node.member}({args})"

def get_node_end_line(node):
    max_line = node.position.line if node.position else 0
    for child in node.children:
        if isinstance(child, (list, tuple)):
            for item in child:
                if isinstance(item, javalang.ast.Node):
                    child_end_line = get_node_end_line(item)
                    if child_end_line > max_line:
                        max_line = child_end_line
        elif isinstance(child, javalang.ast.Node):
            child_end_line = get_node_end_line(child)
            if child_end_line > max_line:
                max_line = child_end_line
    return max_line

def get_package_name(tree):
    if tree.package:
        return tree.package.name
    return ""

def get_class_name(tree):
    for path, node in tree.filter(javalang.tree.ClassDeclaration):
        return node.name
    return "UnknownClass"

def generate_string_id(package, class_name, field_name, suffix=None):
    # Remove redundant package prefix
    package = package.lower()
    if package.startswith(REDUNDANT_PACKAGE_PREFIX):
        package = package[len(REDUNDANT_PACKAGE_PREFIX):]
    class_name = class_name.lower()
    field_name = field_name.lower()
    string_id_base = f"{package}.{class_name}.{field_name}"
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

    # Write processed files list
    with open('processed_files_name_info.txt', 'w', encoding='utf-8') as f:
        for file_path in processed_files:
            f.write(file_path + '\n')

def replace_assignment_in_code(code_lines, node, replacement):
    if node.position:
        start_line = node.position.line - 1
        end_line = get_node_end_line(node) - 1
        # Replace the lines from start_line to end_line with the replacement
        code_lines[start_line:end_line + 1] = [replacement]
    else:
        print(f"Node position is None for assignment")
        return

def main():
    current_directory = os.getcwd()
    print(f"Starting scan in directory: {current_directory}")
    scan_directory(current_directory)
    write_output()
    print("Processing complete.")

if __name__ == "__main__":
    main()
