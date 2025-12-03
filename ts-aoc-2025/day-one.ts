import { mod } from "./math.ts";

export type Rotation = {
  direction: "L" | "R";
  steps: number;
};

export function parse(input: string): Rotation[] {
  return input.split("\n").map((part) => {
    const direction = part.charAt(0) as "L" | "R";
    const steps = parseInt(part.slice(1), 10);
    return { direction, steps };
  });
}

export function solvePartOne(rotations: Rotation[]): number {
    let dial = 50;
    let zeros = 0;
    for (const rotation of rotations) {
        if (rotation.direction === "L") {
            dial -= rotation.steps;
        } else {
            dial += rotation.steps;
        }
        dial = (dial + 100) % 100;
        if (dial === 0) {
            zeros += 1;
        }
    }
    return zeros;
}

export function solvePartTwo(rotations: Rotation[]): number {
    let dial = 50;
    let zeros = 0;
    for (const { direction, steps } of rotations) {
        if (direction === "L") {
            const overshoot = steps - dial;
            if (overshoot >= 0) {
                const clicks = (dial > 0 ? 1 : 0) + Math.floor(overshoot / 100);
                zeros += clicks;
            }
            dial -= steps;
        } else {
            const overshoot = dial + steps - 100;
            if (overshoot >= 0) {
                const clicks = 1 + Math.floor(overshoot / 100);
                zeros += clicks;
            }
            dial += steps;
        }
        dial = mod(dial, 100);
    }
    return zeros;
}