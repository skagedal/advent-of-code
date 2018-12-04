import Foundation

func day4() throws {
    let data = try Data(file: .day4)
    
    let a = day4aValue(data)
    print("4A: \(a)")
}

func day4aValue(_ data: Data) -> Int {
    // Read data and sort it.
    
    let decoder = GuardEventDecoder()
    let sortedEvents = data.lines.map({ decoder.decode($0) }).sorted()
    
    let statistics = calculateGuardStatistics(sortedEvents)
    guard let maxStats = statistics.max(by: { lhs, rhs in
        lhs.value.totalMinutes < rhs.value.totalMinutes
    }) else {
        fatalError("Couldn't find a most sleepy guard")
    }
    
    let mostSleepyGuard = maxStats.key
    
    guard let mostSleepyMinute = maxStats.value.mostSleepyMinute else {
        fatalError("Couldn't find a most sleepy minute")
    }
    return mostSleepyGuard * mostSleepyMinute
}

private func calculateGuardStatistics(_ sortedEvents: [GuardEvent]) -> [GuardIdentifier: GuardStatistics] {
    enum State {
        case initial, inShift(GuardIdentifier), asleep(GuardIdentifier, Minute)
    }
    
    var statistics: [GuardIdentifier: GuardStatistics] = [:]
    var currentState: State = .initial

    print("Let's loop through the events")
    for event in sortedEvents {
        print(event)
        switch (currentState, event.event) {
        case (.initial, .beginsShift(let identifier)),
             (.inShift, .beginsShift(let identifier)):
            currentState = .inShift(identifier)
        case (.inShift(let identifier), .fallsAsleep):
            currentState = .asleep(identifier, event.dateTime.minute)
        case (.asleep(let identifier, let minute), .wakesUp):
            statistics[identifier, default: .zero].addMinutes(fallsAsleepMinute: minute,
                                                              wakeUpMinute: event.dateTime.minute)
            currentState = .inShift(identifier)
        default:
            fatalError("Ignoring illegal event \(event) in state \(currentState)")
        }
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
    
    var totalMinutes: Int {
        return minutes.values.reduce(0, +)
    }
    
    var mostSleepyMinute: Minute? {
        return minutes.max(by: { $0.value < $1.value })?.key
    }
}

// MARK: - Data structures

typealias GuardIdentifier = Int
typealias Year = Int
typealias Month = Int
typealias Day = Int
typealias Hour = Int
typealias Minute = Int

struct GuardEvent {
    struct DateTime {
        let year: Year
        let month: Month
        let day: Day
        let hour: Hour
        let minute: Minute
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
        guard lhs.year == rhs.year else {
            return lhs.year < rhs.year
        }
        guard lhs.month == rhs.month else {
            return lhs.month < rhs.month
        }
        guard lhs.day == rhs.day else {
            return lhs.day < rhs.day
        }
        guard lhs.hour == rhs.hour else {
            return lhs.hour < rhs.hour
        }
        guard lhs.minute == rhs.minute else {
            return lhs.minute < rhs.minute
        }
        return false
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
        guard lhs.dateTime == rhs.dateTime else {
            return lhs.dateTime < rhs.dateTime
        }
        return lhs.event < rhs.event
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

