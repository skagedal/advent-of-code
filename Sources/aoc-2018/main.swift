import Foundation

let url = URL(fileURLWithPath: "Data/day1_input.txt")
let data = try Data(contentsOf: url)
let newLine: UInt8 = 10

let result = data
    .splitSequence(separator: newLine)
    .lazy
    .map({ String(data: $0, encoding: .utf8)! })
    .map({ Int($0)! })
    .reduce(0, +)

print(result)
