package tech.spiro.addrparser.common;

import tech.spiro.addrparser.parser.DefaultContainPointJudge;

/**
 * @author Spiro Huang
 * @since 1.0
 */
public class ContainPointJudgeFactory {

    public static ContainPointJudge create() {
        return new DefaultContainPointJudge();
    }
}
