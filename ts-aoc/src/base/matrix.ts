export type Matrix2D = [number, number, number, number, number, number]
export type Vector2D = [number, number]

export function multiply([a1, b1, c1, a2, b2, c2]: Matrix2D, [x, y]: Vector2D): Vector2D {
    return [
        a1 * x + b1 * y + c1,
        a2 * x + b2 * y + c2
    ];
}

export function rotate90([x, y]: Vector2D): Vector2D {
    return [-y, x];
}
