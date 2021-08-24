package app;

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;

import java.text.ParseException;
import java.util.Objects;

@BuildParseTree
class RuleParser extends BaseParser<Object> {
    private RuleParser parser;

    public void setParser(RuleParser parser) {
        this.parser = parser;
    }

    public RuleParser(RuleParser parser) {
        this.parser = parser;
    }

    public String getRule(String input) {
        String parsed = "";
        parsed = parse(input);
        return parsed;
    }

    public RuleParser() {
    }

    public String parse(String input) {
        ParsingResult<?> result = new ReportingParseRunner(parser.Expression()).run(input);
        String parsed = ParseTreeUtils.printNodeTree(result);
        if (parsed == "") {
            return parsed;
        }
        parsed = parsed.split("\n")[0]
                .split(" ")[1]
                .replace("\'", "");
        return parsed;
    }

    Rule Expression() {
        return Sequence(
                Line(),
                EquityOperator(),
                Line()
        );
    }

    Rule EquityOperator() {
        return FirstOf("<=>", "=>");
    }

    Rule Operator() {
        return FirstOf("^", "+", "|");
    }


    Rule Line() {
        return Sequence(
                Term(),
                ZeroOrMore(Operator(), Term())
        );

    }

    Rule Term() {
        return FirstOf(
                VariableWithNegative(),
                Sequence(Optional(Ch('!')),'(', Line(), ')')
        );
    }

    Rule VariableWithNegative() {
        return Sequence(Optional(Ch('!')), CharRange('A', 'Z'));
    }

}