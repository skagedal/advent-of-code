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

function parseInstruction(instruction: string): Instruction {
    const turn = instruction[0] as Turn;
    const distance = Number(instruction.slice(1));
    return {turn, distance};
}

function handleInstruction({position, direction}: Turtle, {turn, distance}: Instruction): Turtle {
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

function distance({position}: Turtle): number {
    return Math.abs(position.x) + Math.abs(position.y);
}

export default class Y2016D01 extends AocDay {
    year = 2016;
    day = 1;
    static description = 'No Time for a Taxicab'

    async part1(): Promise<number> {
        const data = await this.readFile();

        const turtle = data.split(', ')
            .map(parseInstruction)
            .reduce(handleInstruction, initialTurtle);

        return distance(turtle);
    }

    async part2(): Promise<number> {
        const data = await this.readFile();

        const instructions = data.split(', ')
            .map(parseInstruction);

        const visited: Position[] = [];
        let turtle = initialTurtle;
        visited.push(turtle.position);

        // TODO: Oh. We need to actually keep track of all visited squares. 

        for (const instruction of instructions) {
            turtle = handleInstruction(turtle, instruction);
            console.log(`After instruction ${JSON.stringify(instruction)}: ${JSON.stringify(turtle)}`)
            if (visited.some(pos => pos.x === turtle.position.x && pos.y === turtle.position.y)) {
                console.log(`Visited ${JSON.stringify(turtle.position)} twice`);
                return distance(turtle);
            }
            visited.push(turtle.position);
        }

        return 0;
    }
}