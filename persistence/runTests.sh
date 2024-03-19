#!/bin/bash

# Define the list of directories
dirs=(
    "test.bee.persistent.blaze"
    "test.bee.persistent.jpa"
    "test.jpa.eager"
    "test.jpa.lazy"
)

# Loop through the directories and run ./gradlew test
for dir in "${dirs[@]}"; do
    echo
    echo "--------------------------------------"
    echo "Testing in directory: $dir"
    echo "--------------------------------------"
    if [ -d "$dir" ]; then
        pushd "$dir" > /dev/null
        if [ -f "./gradlew" ]; then
            ./gradlew test -i --rerun
        else
            echo "gradlew not found in $dir"
        fi
        popd > /dev/null
    else
        echo "Directory $dir not found."
    fi
done

echo
echo "All tests completed."
