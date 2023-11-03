import { expect } from 'chai';
import { Vector2D, rotate90 } from '../../src/base/matrix';

describe('Matrix tests', () => {
    it('should rotate a point', () => {
        const point = [ 1, 0 ] as Vector2D;
        const rotatedPoint = rotate90(point);
        expect(rotatedPoint).to.deep.equal([ 0, 1 ]);
    });
});
