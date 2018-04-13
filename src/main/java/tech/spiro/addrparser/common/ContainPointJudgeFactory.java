package tech.spiro.addrparser.common;

import tech.spiro.addrparser.parser.DefaultContainPointJudge;

/**
 * @Author: Shaoping Huang
 * @Description:
 * @Date: 4/10/2018
 */
public class ContainPointJudgeFactory {

    public static ContainPointJudge create() {
        return new DefaultContainPointJudge();
    }
}
