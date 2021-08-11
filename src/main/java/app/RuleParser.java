package app;

import org.parboiled.BaseParser;
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
        try {
            parsed = parse(input);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        return parsed;
    }

    public String parse(String input) throws ParseException {
        ParsingResult<?> result = new ReportingParseRunner(parser.Expression()).run(input);
        String parsed = ParseTreeUtils.printNodeTree(result);
        if (parsed == "") {
            throw new ParseException("parsing error: invalid format", 0);
        }
        parsed = parsed.split("\n")[0]
                .split(" ")[1]
                .replace("\'", "");
        if (!parsed.equals(input)) {
            int i = -1;
            int length = Math.min(parsed.length(), input.length());
            while (++i < length) {
                if (parsed.charAt(i) != input.charAt(i)) {
                    break ;
                }
            }
            throw new ParseException(String.format(
                    "invalid character at position %d: %c",
                    i, input.charAt(i)), i);
        }
        return parsed;
    }

    public RuleParser() {}

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
                Sequence('(', Line(), ')')
        );
    }

    Rule VariableWithNegative() {
        return Sequence(Optional(Ch('!')), CharRange('A', 'Z'));
    }

}