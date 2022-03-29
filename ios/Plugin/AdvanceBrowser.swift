import Foundation

@objc public class AdvanceBrowser: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
