import { Command } from "@oclif/core";
import Y2016D01 from "../../days/2016/d1.js";
import { AocDay } from "../../base/AocDay.js";

export default class RunCommand extends Command {
    static description = 'Advent of Code 2016'

    async run(): Promise<void> {
        console.log('Advent of Code 2016');
        const day: AocDay = new Y2016D01();
        console.log(await day.part1());
        if (day.part2 !== undefined) {
            console.log(await day.part2());
        }
    }

}