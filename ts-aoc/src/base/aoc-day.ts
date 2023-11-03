import { readFile } from "node:fs/promises";
import { homedir } from "node:os";

export abstract class AocDay {
    abstract year: number;
    abstract day: number;
    static foo: number;

    async readFile(): Promise<string> {
        const day = String(this.day).padStart(2, '0')
        const path = `${homedir()}/.aoc/data/year${this.year}/day${day}_input.txt`;

        return readFile(path, { encoding: 'utf8'})
    }

    abstract part1(): Promise<string | number>;
    abstract part2(): Promise<string | number>;
}