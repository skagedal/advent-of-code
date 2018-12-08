import Foundation

struct Day08: AdventDay2018 {
    let day = 8
    let knownAnswerToExampleForFirstPart = "138"
    let knownAnswerToFirstPart = "41454"
    let knownAnswerToExampleForSecondPart = "66"
    let knownAnswerToSecondPart = "25752"
    
    func answerToFirstPart(_ data: Data) throws -> String {
        return LicenseTree
            .parse(data)
            .metadataSum()
            .toString
    }
    
    func answerToSecondPart(_ data: Data) throws -> String {
        return LicenseTree
            .parse(data)
            .value()
            .toString
    }
}

private struct LicenseTree {
    let children: [LicenseTree]
    let metadata: [Int]
}

extension LicenseTree {
    static func parse(_ data: Data) -> LicenseTree {
        let ints = String(data: data, encoding: .utf8)!.split(separator: " ").map({ Int($0)! })
        var iterator = ints.makeIterator()
        return LicenseTree.parse(iterator: &iterator)
    }
    
    static func parse(iterator: inout IndexingIterator<[Int]>) -> LicenseTree {
        let numberOfChildren = iterator.next()!
        let numberOfMetadata = iterator.next()!
        let children = Array(count: numberOfChildren, createdBy: { LicenseTree.parse(iterator: &iterator) })
        let metadata = Array(count: numberOfMetadata, createdBy: { iterator.next()! })
        return LicenseTree(children: children, metadata: metadata)
    }
    
    func metadataSum() -> Int {
        return metadata.sum() + children.map({ $0.metadataSum() }).sum()
    }

    func value() -> Int {
        if children.isEmpty {
            return metadata.sum()
        } else {
            return metadata.map({ pointer in
                let index = pointer - 1
                if children.indices.contains(index) {
                    return children[index].value()
                } else {
                    return 0
                }
            }).sum()
        }
    }
}
