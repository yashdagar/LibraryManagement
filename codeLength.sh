#!/bin/bash

# Check if a directory is provided
if [ $# -eq 0 ]; then
  DIR="."
else
  DIR="$1"
fi

# Find all .dart files recursively and count characters
total_chars=0
while IFS= read -r file; do
  # Count characters in the file
  chars=$(wc -m < "$file")
  echo "File: $file - $chars characters"
  total_chars=$((total_chars + chars))
done < <(find "$DIR" -type f -name "*.dart")

echo "Total characters in all .dart files: $total_chars"
