import { readFile } from "fs/promises";
import { homedir } from "os";

export abstract class AocDay {
    abstract year: number;
    abstract day: number;
    static foo: number;

    async readFile(): Promise<string> {
        const day = String(this.day).padStart(2, '0')
        const path = `${homedir()}/.aoc/data/year${this.year}/day${day}_input.txt`;

        return await readFile(path, { encoding: 'utf-8'})
    }
}