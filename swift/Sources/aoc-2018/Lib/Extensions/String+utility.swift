extension String {
    func substringsOverlap(_ a: Substring, _ b: Substring) -> Bool {
        let aStart = a.startIndex.samePosition(in: self)!
        let bStart = b.startIndex.samePosition(in: self)!
        let aEnd = a.endIndex.samePosition(in: self)!
        let bEnd = b.endIndex.samePosition(in: self)!
        
        if aStart < bStart {
            return aEnd > bStart
        } else  {
            return bEnd > aStart
        }
    }
}
