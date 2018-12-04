import Foundation

/// A simple, transparent wrapper around NSRegularExpression
struct RegularExpression {
    struct Match {
        let string: String
        let ns: NSTextCheckingResult
        
        init(string: String, result: NSTextCheckingResult) {
            self.string = string
            self.ns = result
        }
        
        subscript(_ index: Int) -> String {
            let range = ns.range(at: index)
            return (string as NSString).substring(with: range)
        }
    }
    
    let ns: NSRegularExpression
    
    init(pattern: String, options: NSRegularExpression.Options = []) throws {
        self.ns = try NSRegularExpression(pattern: pattern, options: options)
    }
    
    func matches(in string: String, options: NSRegularExpression.MatchingOptions = []) -> [Match] {
        let range = NSRange(location: 0, length: (string as NSString).length)
        func match(_ result: NSTextCheckingResult) -> Match {
            return Match(string: string, result: result)
        }
        return ns.matches(in: string, options: options, range: range).map(match)
    }
}
