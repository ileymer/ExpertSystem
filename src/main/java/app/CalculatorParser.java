package app;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

@BuildParseTree
class CalculatorParser extends BaseParser<Object> {


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