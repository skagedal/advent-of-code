import { Command } from "@oclif/core";
import { AocDay } from "../../base/AocDay.js";

type Turn = 'L' | 'R';
type Direction = 'N' | 'E' | 'S' | 'W';
type Position = { x: number, y: number };
type Turtle = { position: Position, direction: Direction }
type Instruction = { turn: Turn, distance: number}
const initialTurtle: Turtle = {
    position: {x: 0, y: 0},
    direction: 'N'
};
const handleInstruction = ({turn, distance}: Instruction) => ({position, direction}: Turtle) => {
    const directions = ['N', 'E', 'S', 'W'];
    const index = (directions.indexOf(direction) + (turn === 'L' ? 3 : 1)) % 4;
    const newDirection = directions[index] as Direction;
    return {
        position: {
            x: position.x + (newDirection === 'E' ? distance : newDirection === 'W' ? -distance : 0),
            y: position.y + (newDirection === 'N' ? distance : newDirection === 'S' ? -distance : 0)
        },
        direction: newDirection
    };
}


export default class Y2016D01 extends AocDay {
    year = 2016;
    day = 1;
    static description = 'No Time for a Taxicab'

    async run(): Promise<void> {
        const data = await this.readFile();
        // const data = 'R2, L3';

        let turtle: Turtle = {
            position: {x: 0, y: 0},
            direction: 'N'
        };
        data.split(', ').forEach((instruction) => {
            const turn = instruction[0] as Turn;
            const distance = Number(instruction.slice(1));
            turtle = handleInstruction({turn, distance})(turtle);
        });

        console.log(Math.abs(turtle.position.x) + Math.abs(turtle.position.y));

    }

}