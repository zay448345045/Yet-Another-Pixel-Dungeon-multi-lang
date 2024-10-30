import os
import re

# Constants
MAX_FILES = 2000
JAVA_FILE_EXTENSIONS = ['.java']

# Globals
file_count = 0
processed_files = []

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

        # Process the code to merge string literals
        code_lines = code.split('\n')
        code_lines = merge_string_literals(code_lines)
        code = '\n'.join(code_lines)

        # Write the modified code back to the file
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(code)

        processed_files.append(file_path)

    except Exception as e:
        print(f"Error processing {file_path}: {e}")

def merge_string_literals(lines):
    new_lines = []
    idx = 0
    while idx < len(lines):
        line = lines[idx]
        stripped_line = line.strip()
        if stripped_line == '':
            new_lines.append(line)
            idx += 1
            continue

        # Check if line is part of a multi-line field declaration (outside methods)
        if is_field_declaration(line):
            # If the declaration ends with a semicolon, it's a single-line declaration and should be skipped
            if ';' in stripped_line:
                new_lines.append(line)
                idx += 1
                continue
            else:
                # Process multi-line field declaration
                statement_lines = [line]
                idx += 1
                while idx < len(lines):
                    next_line = lines[idx]
                    statement_lines.append(next_line)
                    if ';' in next_line:
                        idx += 1
                        break
                    idx += 1
                # Process the field declaration
                new_lines.extend(process_field_declaration(statement_lines))
                continue

        # Check if line is a method declaration
        if is_method_start(line):
            # Collect method lines
            method_lines = [line]
            brace_count = line.count('{') - line.count('}')
            idx += 1
            while brace_count > 0 and idx < len(lines):
                next_line = lines[idx]
                method_lines.append(next_line)
                brace_count += next_line.count('{') - next_line.count('}')
                idx += 1
            # Process the method
            new_method_lines = process_method(method_lines)
            new_lines.extend(new_method_lines)
            continue

        # Otherwise, just add the line
        new_lines.append(line)
        idx += 1

    return new_lines

def is_field_declaration(line):
    # Check for field declaration assigning to String that does not end with a semicolon
    # We only want to process multi-line declarations
    return (re.match(r'\s*(public|protected|private)?\s*(static\s+)?(final\s+)?String\s+\w+\s*=', line) and
            ';' not in line)

def process_field_declaration(lines):
    # Join the lines to form the complete statement
    statement = ''.join(lines)
    # Process the assignment
    match = re.match(r'(.*?=\s*)(.*);', statement, re.DOTALL)
    if match:
        prefix = match.group(1)
        expr = match.group(2)
        new_expr = process_expression(expr)
        new_statement = prefix + new_expr + ';'
        indent = re.match(r'(\s*)', lines[0]).group(1)
        return [indent + new_statement]
    return lines

def is_method_start(line):
    # Check if the line is a method declaration
    return re.match(r'\s*(public|protected|private)?\s*(static\s+)?\w+\s+\w+\s*\(.*\)\s*\{', line)

def process_method(lines):
    # First, get the method signature to check the return type
    method_signature = lines[0]
    return_type = get_method_return_type(method_signature)
    is_string_method = return_type == 'String'
    # If it's not a String method, we won't process its return statements
    if not is_string_method:
        return lines

    # Process each line inside the method
    new_lines = []
    idx = 0
    while idx < len(lines):
        line = lines[idx]
        stripped_line = line.strip()
        if stripped_line == '':
            new_lines.append(line)
            idx += 1
            continue

        if 'return' in stripped_line:
            # Start collecting a return statement
            statement_lines = [line]
            # Check if the line ends with a semicolon
            if ';' in stripped_line:
                # Process the return statement
                new_lines.extend(process_return_statement(statement_lines))
                idx += 1
            else:
                # Collect the rest of the return statement
                idx += 1
                while idx < len(lines):
                    next_line = lines[idx]
                    statement_lines.append(next_line)
                    if ';' in next_line:
                        idx += 1
                        break
                    idx += 1
                new_lines.extend(process_return_statement(statement_lines))
        else:
            new_lines.append(line)
            idx += 1

    return new_lines

def get_method_return_type(signature_line):
    # Extract the return type from the method signature
    match = re.match(r'\s*(public|protected|private)?\s*(static\s+)?(\w+)\s+\w+\s*\(.*\)\s*\{', signature_line)
    if match:
        return match.group(3)
    return None

def process_return_statement(lines):
    # Join the lines into a single statement
    statement = ''.join(lines)
    # Process only if it's a return statement
    match = re.match(r'(.*?return\s+)(.*);', statement, re.DOTALL)
    if match:
        prefix = match.group(1)
        expr = match.group(2)
        new_expr = process_expression(expr)
        new_statement = prefix + new_expr + ';'
        indent = re.match(r'(\s*)', lines[0]).group(1)
        return [indent + new_statement]
    else:
        return lines

def process_expression(expr):
    # Split the expression into tokens separated by '+'
    tokens = re.split(r'(\+)', expr)
    new_tokens = []
    i = 0
    while i < len(tokens):
        token = tokens[i]
        if token.strip() == '+':
            i += 1
            continue
        token = token.strip()
        if token.startswith('"'):
            # Start of a string literal
            concatenated = token
            i += 1
            while i < len(tokens) and tokens[i].strip() == '+' and i+1 < len(tokens) and tokens[i+1].strip().startswith('"'):
                concatenated = concatenated.rstrip('"') + tokens[i+1].strip().lstrip('"')
                i += 2
            new_tokens.append(concatenated)
        else:
            new_tokens.append(token)
            i += 1
    # Reconstruct the expression
    new_expr = ' + '.join(new_tokens)
    return new_expr

def scan_directory(directory):
    global file_count
    for root, dirs, files in os.walk(directory):
        for file in files:
            if any(file.endswith(ext) for ext in JAVA_FILE_EXTENSIONS):
                file_path = os.path.join(root, file)
                process_java_file(file_path)
                if file_count >= MAX_FILES:
                    return

def main():
    current_directory = os.getcwd()
    print(f"Starting scan in directory: {current_directory}")
    scan_directory(current_directory)
    print("Processing complete.")

if __name__ == "__main__":
    main()
