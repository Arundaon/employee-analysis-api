import json
# taruh file "Employees50k.json" ke dalam directory yang sama
def process_bulk_file(input_file, output_file):
    with open(input_file, 'r') as infile, open(output_file, 'w') as outfile:
        for line in infile:
            try:
                data = json.loads(line)
            except json.JSONDecodeError:
                outfile.write(line)
                continue

            if 'index' in data and '_type' in data['index']:
                del data['index']['_type']
                outfile.write(json.dumps(data) + '\n')
            else:
                outfile.write(line)

input_file = 'Employees50K.json'
output_file = 'Employees50K_modified.json'

process_bulk_file(input_file, output_file)

print(f'Processed {input_file} and saved the result to {output_file}')