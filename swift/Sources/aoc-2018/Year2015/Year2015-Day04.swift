import Foundation
import CryptoKit

extension Year2015 {
    struct Day04: AdventDay2015 {
        let day = 4

        func answerToFirstPart(_ data: Data) throws -> String {
            return findMD5(messagePrefix: data.firstLineAsData, matching: hasFiveZeroNibblePrefix).toString
        }
    
        func answerToSecondPart(_ data: Data) throws -> String {
            return findMD5(messagePrefix: data.firstLineAsData, matching: hasSixZeroNibblePrefix).toString
        }

        private func findMD5(messagePrefix: Data, matching predicate: ((Insecure.MD5Digest) -> Bool)) -> Int {
            var counter = 1
            while (!predicate(makeDigest(messagePrefix, counter: counter))) {
                counter += 1;
            }
            
            return counter
        }
        
        let knownAnswerToExampleForFirstPart = "609043"
        let knownAnswerToFirstPart = "346386"
        let knownAnswerToSecondPart = "9958218"
    }
}

@inlinable
func makeDigest(_ prefix: Data, counter: Int) -> Insecure.MD5Digest {
    let message = prefix + "\(counter)".data(using: .ascii)!
    return Insecure.MD5.hash(data: message)
}

@inlinable
func hasFiveZeroNibblePrefix(_ data: Insecure.MD5Digest) -> Bool {
    return data.withUnsafeBytes { unsafeRawBufferPointer in
        let int64 = UInt64(bigEndian: unsafeRawBufferPointer.load(as: UInt64.self))
        return int64.leadingZeroBitCount >= 20;
    }
}

@inlinable
func hasSixZeroNibblePrefix(_ data: Insecure.MD5Digest) -> Bool {
    return data.withUnsafeBytes { unsafeRawBufferPointer in
        let int64 = UInt64(bigEndian: unsafeRawBufferPointer.load(as: UInt64.self))
        return int64.leadingZeroBitCount >= 24;
    }
}
