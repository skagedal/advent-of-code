// example input: 11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124

export type Range = {
  start: number;
  end: number;
};

export function parseDayTwo(input: string): Range[] {
  return input.split(",").map((part) => {
    const [start, end] = part.split("-").map(Number);
    return { start, end };
  });
}

export function isInvalid(number: number): boolean {
  const stringified = number.toString();
  if (stringified.length % 2 === 0) {
    const mid = stringified.length / 2;
    const firstHalf = stringified.slice(0, mid);
    const secondHalf = stringified.slice(mid);
    return firstHalf === secondHalf;
  }
  return false;
}

export function solveDayTwoPartOne(ranges: Range[]): number {
  let invalidSum = 0;
  for (const { start, end } of ranges) {
    for (let num = start; num <= end; num++) {
      if (isInvalid(num)) {
        invalidSum += num;
      }
    }
  }
  return invalidSum;
}
