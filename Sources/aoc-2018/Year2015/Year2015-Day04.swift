import Foundation
import CommonCrypto

extension Year2015 {
    struct Day04: AdventDay2015 {
        let day = 4

        func answerToFirstPart(_ data: Data) throws -> String {
            return findMD5(messagePrefix: data, matching: hasFiveZeroNibblePrefix).toString
        }
    
        func answerToSecondPart(_ data: Data) throws -> String {
            return findMD5(messagePrefix: data, matching: hasSixZeroNibblePrefix).toString
        }

        private func findMD5(messagePrefix: Data, matching predicate: ((Data) -> Bool)) -> Int {
            var digestData = Data(count: Int(CC_MD5_DIGEST_LENGTH))
            
            var counter = 0
            repeat {
                counter += 1
                let message = messagePrefix + "\(counter)".data(using: .ascii)!
                _ = digestData.withUnsafeMutableBytes { digestBytes in
                    message.withUnsafeBytes { messageBytes in
                        CC_MD5(messageBytes, CC_LONG(message.count), digestBytes)
                    }
                }
            } while !predicate(digestData)
            
            return counter
        }
        
        let knownAnswerToExampleForFirstPart = "609043"
        let knownAnswerToFirstPart = "117946"
        let knownAnswerToSecondPart = "3938038"
    }
}

@inlinable
func hasFiveZeroNibblePrefix(_ data: Data) -> Bool {
    return data.withUnsafeBytes { (ptr: UnsafePointer<UInt64>) -> Bool in
        return ptr.pointee.bigEndian & 0xFFFF_F000_0000_0000 == 0
    }
}

@inlinable
func hasSixZeroNibblePrefix(_ data: Data) -> Bool {
    return data.withUnsafeBytes { (ptr: UnsafePointer<UInt64>) -> Bool in
        return ptr.pointee.bigEndian & 0xFFFF_FF00_0000_0000 == 0
    }
}
