import XCTest

#if !os(macOS)
public func allTests() -> [XCTestCaseEntry] {
    return [
        testCase(aoc_2018Tests.allTests),
    ]
}
#endif