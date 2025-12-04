import { readFileSync } from "node:fs";
import { parseDayOne, solvePartOne as solveDayOnePartOne, solvePartTwo as solveDayOnePartTwo } from "./day-one.ts";
import { parseDayTwo, solveDayTwoPartOne } from "./day-two.ts";

function dayOnePartOne() {
    const data = readFileSync("/Users/simon/.aoc/data/year2025/day01_input.txt", "utf-8");
    const rotations = parseDayOne(data);
    const result = solveDayOnePartOne(rotations);
    console.log(`Day 1 part 1: ${result}`);
}

function dayOnePartTwo() {
    const data = readFileSync("/Users/simon/.aoc/data/year2025/day01_input.txt", "utf-8");
    const rotations = parseDayOne(data);
    const result = solveDayOnePartTwo(rotations);
    console.log(`Day 1 part 2: ${result}`);
}

function dayTwoPartOne() {
    const data = readFileSync("/Users/simon/.aoc/data/year2025/day02_input.txt", "utf-8");
    const rotations = parseDayTwo(data);
    const result = solveDayTwoPartOne(rotations);
    console.log(`Day 2 part 1: ${result}`);
}

dayOnePartOne();
dayOnePartTwo();
dayTwoPartOne();
