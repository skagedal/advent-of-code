import assert from "node:assert";
import test from "node:test";
import { parseDayOne, solvePartOne, solvePartTwo, type Rotation } from "./day-one.ts";

const example = `L68
L30
R48
L5
R60
L55
L1
L99
R14
L82`;

const exampleParsed: Rotation[] = [
  { direction: "L", steps: 68 },
  { direction: "L", steps: 30 },
  { direction: "R", steps: 48 },
  { direction: "L", steps: 5 },
  { direction: "R", steps: 60 },
  { direction: "L", steps: 55 },
  { direction: "L", steps: 1 },
  { direction: "L", steps: 99 },
  { direction: "R", steps: 14 },
  { direction: "L", steps: 82 },
];

test("parse", (t) => {
  const parsed = parseDayOne(example);
  assert.deepStrictEqual(parsed, exampleParsed);
});

test("solvePartOne", (t) => {
  const result = solvePartOne(exampleParsed);
  assert.strictEqual(result, 3);
});

test("solvePartTwo", (t) => {
  const result = solvePartTwo(exampleParsed);
  assert.strictEqual(result, 6);
});
