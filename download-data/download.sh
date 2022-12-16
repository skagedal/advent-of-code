#!/usr/bin/env bash

data_dir="${HOME}/.aoc/data"

for year in $(seq 2015 2022); do
    echo "== $year =="
    year_dir="$data_dir/year$year"
    mkdir -p "$year_dir"
    for day in $(seq 1 25); do
        day_file=$(printf "$year_dir/day%02d_input.txt" $day)
        if test -f "$day_file"; then
            echo "$day_file already exists - skipping"
        elif [ "$(gdate --date="$(printf "%s-12-%02d EST" $year $day)" +%s)" -gt "$(gdate +%s)" ]; then
            echo "Not downloading $day_file - time has not come"
        else
            curl -s -S -f "https://adventofcode.com/$year/day/$day/input" -H "$COOKIE" -o $day_file
            echo "Downloaded $day_file"
            sleep 2
        fi
    done
done
