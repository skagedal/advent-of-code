import { readFileSync } from "node:fs";
import { parse, solvePartOne, solvePartTwo } from "./day-one.ts";

function dayOnePartOne() {
    const data = readFileSync("/Users/simon/.aoc/data/year2025/day01_input.txt", "utf-8");
    const rotations = parse(data);
    const result = solvePartOne(rotations);
    console.log(`Day 1 part 1: ${result}`);
}

function dayOnePartTwo() {
    const data = readFileSync("/Users/simon/.aoc/data/year2025/day01_input.txt", "utf-8");
    const rotations = parse(data);
    const result = solvePartTwo(rotations);
    console.log(`Day 1 part 2: ${result}`);
}


dayOnePartOne();
dayOnePartTwo();