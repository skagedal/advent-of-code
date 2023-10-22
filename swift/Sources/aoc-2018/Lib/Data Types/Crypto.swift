import Foundation
import CommonCrypto

// Was used in Year 2015 - Day 04, but then that was optimized.  Keeping here for fun.
func MD5(string: String) -> Data {
    let messageData = string.data(using:.utf8)!
    var digestData = Data(count: Int(CC_MD5_DIGEST_LENGTH))
    
    _ = digestData.withUnsafeMutableBytes {(digestBytes: UnsafeMutableRawBufferPointer) in
        messageData.withUnsafeBytes {(messageBytes: UnsafeRawBufferPointer) in
            CC_MD5(messageBytes.baseAddress, CC_LONG(messageData.count), digestBytes.bindMemory(to: UInt8.self).baseAddress)
        }
    }
    
    return digestData
}

