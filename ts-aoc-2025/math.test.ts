import assert from "node:assert";
import test from "node:test";
import { mod } from "./math.ts";

test("mod", () => {
  assert.strictEqual(mod(5, 3), 2);
  assert.strictEqual(mod(-1, 3), 2);
  assert.strictEqual(mod(7, 5), 2);
  assert.strictEqual(mod(-3, 5), 2);
  assert.strictEqual(mod(0, 4), 0);
  assert.strictEqual(mod(-207, 100), 93);
});
