import Foundation

struct Day04: AdventDay {
    let day = 4
    let knownAnswerToExampleForFirstPart = "240"
    let knownAnswerToFirstPart = "14346"
    let knownAnswerToExampleForSecondPart = "4455"
    let knownAnswerToSecondPart = "5705"
    
    func answerToFirstPart(_ data: Data) throws -> String {
        return puzzleSolution(data, statisticsKey: \.totalSleepMinutes).toString
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        return puzzleSolution(data, statisticsKey: \.daysOfMostSleepyMinute).toString
    }
}

private func puzzleSolution(_ data: Data, statisticsKey: KeyPath<GuardStatistics, Int>) -> Int {
    let decoder = GuardEventDecoder()
    let sortedEvents = data.lines.map({ decoder.decode($0) }).sorted()
    
    let statistics = calculateGuardStatistics(sortedEvents)
    let sleepyStats = statistics.max(byValue: statisticsKey)!
    let sleepyGuard = sleepyStats.key
    let sleepyMinute = sleepyStats.value.mostSleepyMinute!
    return sleepyGuard * sleepyMinute
}

private func calculateGuardStatistics(_ sortedEvents: [GuardEvent]) -> [GuardIdentifier: GuardStatistics] {
    enum State {
        case initial, working(GuardIdentifier), asleep(GuardIdentifier, Minute)
    }
    
    var statistics: [GuardIdentifier: GuardStatistics] = [:]
    var currentState: State = .initial

    for event in sortedEvents {
        currentState = {
            switch(currentState, event.event) {
                
            case (.initial, .beginsShift(let `guard`)),
                 (.working, .beginsShift(let `guard`)):
                return .working(`guard`)
                
            case (.working(let `guard`), .fallsAsleep):
                return .asleep(`guard`, event.dateTime.minute)
                
            case (.asleep(let `guard`, let minute), .wakesUp):
                statistics[`guard`, default: .zero].addMinutes(fallsAsleepMinute: minute,
                                                               wakeUpMinute: event.dateTime.minute)
                return .working(`guard`)

            default:
                fatalError("Event \(event) illegal in state \(currentState)")
            }
        }()
    }
    return statistics
}

struct GuardStatistics {
    var minutes: [Minute: Int]

    static let zero = GuardStatistics(minutes: [:])
    
    mutating func addMinutes(fallsAsleepMinute: Minute, wakeUpMinute: Minute) {
        for minute in fallsAsleepMinute..<wakeUpMinute {
            minutes[minute, default: 0] += 1
        }
    }
    
    var totalSleepMinutes: Int {
        return minutes.values.reduce(0, +)
    }
    
    var mostSleepyMinute: Minute? {
        return minutes.max(by: { $0.value < $1.value })?.key
    }
    
    var daysOfMostSleepyMinute: Int {
        return minutes[mostSleepyMinute!]!
    }
}

// MARK: - Data structures

typealias GuardIdentifier = Int
typealias Minute = Int

struct GuardEvent {
    struct DateTime {
        let year: Int
        let month: Int
        let day: Int
        let hour: Int
        let minute: Int
    }
    
    enum Event {
        case wakesUp
        case fallsAsleep
        case beginsShift(GuardIdentifier)
    }
    
    let dateTime: DateTime
    let event: Event
}

extension GuardEvent.DateTime: Comparable {
    static func < (lhs: GuardEvent.DateTime, rhs: GuardEvent.DateTime) -> Bool {
        return isLessThan(for: [\.year, \.month, \.day, \.hour, \.minute])(lhs, rhs)
    }
}

extension GuardEvent.Event: Comparable {
    /// This sorting is arbitrary, but it's nice to have a predictable sort of GuardEvent.
    /// beginsShift < fallsAsleep < wakesUp.
    static func < (lhs: GuardEvent.Event, rhs: GuardEvent.Event) -> Bool {
        switch (lhs, rhs) {
        case let (.beginsShift(a), .beginsShift(b)):
            return a < b
        case (.beginsShift, _), (.fallsAsleep, .wakesUp):
            return true
        case (.fallsAsleep, _), (.wakesUp, _):
            return false
        }
    }
}

extension GuardEvent: Comparable {
    static func < (lhs: GuardEvent, rhs: GuardEvent) -> Bool {
        return (lhs.dateTime, lhs.event) < (rhs.dateTime, rhs.event)
    }
}

extension GuardEvent.DateTime: CustomStringConvertible {
    var description: String {
        return String(format: "%04d-%02d-%02d %02d:%02d", year, month, day, hour, minute)
    }
}

extension GuardEvent: CustomStringConvertible {
    var description: String {
        return "[\(dateTime)] - \(event)"
    }
}

// MARK: - Parser

class GuardEventDecoder {
    let dateTimeRegex = try! RegularExpression(pattern: "^\\[(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d) (\\d\\d):(\\d\\d)\\] ")
    let wakesUpRegex = try! RegularExpression(pattern: " wakes up$")
    let fallsAsleepRegex = try! RegularExpression(pattern: " falls asleep$")
    let beginsShiftRegex = try! RegularExpression(pattern: " Guard #(\\d+) begins shift$")
    
    func decode(_ string: String) -> GuardEvent {
        return GuardEvent(dateTime: decodeDateTime(string),
                          event: decodeEvent(string))
    }
    
    func decodeDateTime(_ string: String) -> GuardEvent.DateTime {
        guard let match = dateTimeRegex.firstMatch(in: string) else {
            fatalError("No Date Time match: \(string)")
        }
        let components = match.ranges().compactMap(Int.init)
        return GuardEvent.DateTime(year: components[0],
                                   month: components[1],
                                   day: components[2],
                                   hour: components[3],
                                   minute: components[4])
    }
    
    func decodeEvent(_ string: String) -> GuardEvent.Event {
        switch string {
        case wakesUpRegex:
            return .wakesUp
        case fallsAsleepRegex:
            return .fallsAsleep
        case beginsShiftRegex:
            return .beginsShift(Int(beginsShiftRegex.firstMatch(in: string)![1])!)
        default:
            fatalError("Must match one of the regexes")
        }
    }
}

