package org.lappsgrid.eval.reporter;

import org.lappsgrid.eval.model.Span;
import org.lappsgrid.eval.model.SpanEvaluation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

public class HtmlReporter {

    static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    static String HTML_PAGE_HEAD = "\n"
            + "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body><div>\n"
            + "<h3>Annotation Diff </h3>";
//          "- comparing annotations - "
//          + df.format(Calendar.getInstance().getTime()) + "</h3> ";

    static String HTML_PAGE_END = "</table></div></body></html>";

    static String TABLE_HEADER = "<table cellpadding=\"0\" border=\"1\">\n" +
            "<tr>\n" +
            "  <th align=\"left\">Start</th>\n" +
            "  <th align=\"left\">End</th>\n" +
            "  <th align=\"left\">Features</th>\n" +
            "  <th align=\"left\">Start</th>\n" +
            "  <th align=\"left\">End</th>\n" +
            "  <th align=\"left\">Features</th>\n" +
            "</tr>";

    static String ONLY_GOLD_COLOR = "bgcolor=\"#ffadb5\"";

    static String ONLY_PREDICT_COLOR = "bgcolor=\"#B4ADFF\"";

    static String WRONG_COLOR = "bgcolor=\"#F8FF78\"";

    static String RIGHT_COLOR = "bgcolor=\"#ffffff\"";

    private static DecimalFormat threePlace = new DecimalFormat("#0.000");

    SpanEvaluation<String> eval;
    Map<Span, String> goldSpanOut;
    Map<Span, String> predictSpanOut;

    public HtmlReporter(Map<Span, String> goldSpanOut, Map<Span, String> predictSpanOut) {
        super();
        this.goldSpanOut = goldSpanOut;
        this.predictSpanOut = predictSpanOut;
        eval = new SpanEvaluation<String>();
        eval.add(goldSpanOut, predictSpanOut);
    }

    public String toHtmlString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Gold output counts: " + eval.countReferenceOutcomes() + "<br>");
        sb.append("Predicted output counts: " + eval.countPredictedOutcomes() + "<br>");
        sb.append("Correct matches: " + eval.countCorrectOutcomes() + "<br><br>");
        sb.append("Precision: " + threePlace.format(eval.precision()) + "<br>");
        sb.append("Recall: " + threePlace.format(eval.recall()) + "<br>");
        sb.append("F1: " + threePlace.format(eval.f1()) + "<br><br>");

        sb.append(TABLE_HEADER);

        HashSet<Span> keySet = new HashSet<>(goldSpanOut.keySet());
        keySet.addAll(predictSpanOut.keySet());

        ArrayList<Span> keyList = new ArrayList<>(keySet);
        Collections.sort(keyList);

        for (Span span : keyList) {
            sb.append("\n<tr>");
            boolean isGoldMatched = goldSpanOut.containsKey(span);
            boolean isPredMatched = predictSpanOut.containsKey(span);
            boolean isMatched = (isGoldMatched && isPredMatched);

            String color = isMatched ?
                    (goldSpanOut.get(span).equalsIgnoreCase(predictSpanOut.get(span))) ? RIGHT_COLOR : WRONG_COLOR :
                    (isGoldMatched ? ONLY_GOLD_COLOR : ONLY_PREDICT_COLOR);


            sb.append("<td ").append(color).append(">").append(isGoldMatched ? span.start : "").append("</td>");
            sb.append("<td ").append(color).append(">").append(isGoldMatched ? span.end : "").append("</td>");
            sb.append("<td ").append(color).append(">").append(isGoldMatched ? goldSpanOut.get(span) : "").append("</td>");

            sb.append("<td ").append(color).append(">").append(isPredMatched ? span.start : "").append("</td>");
            sb.append("<td ").append(color).append(">").append(isPredMatched ? span.end : "").append("</td>");
            sb.append("<td ").append(color).append(">").append(isPredMatched ? predictSpanOut.get(span) : "").append("</td>");

            sb.append("\n</tr>");
        }

        sb.insert(0, HTML_PAGE_HEAD).append(HTML_PAGE_END);
        return sb.toString();
    }

    public void saveToHtmlFile(String filepath) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(filepath);
        out.println(toHtmlString());
        out.close();
    }


}