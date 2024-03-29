/*-
 * Copyright 2014 The Language Application Grid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.lappsgrid.eval.reporter;

import org.json.simple.JSONObject;
import org.lappsgrid.eval.model.Span;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonReporter extends Reporter {

    public JsonReporter(Map<Span, String> goldSpanOut, Map<Span, String> predictSpanOut) {
        super(goldSpanOut, predictSpanOut);
    }

    @Override
    public String report() {
        JSONObject obj = new JSONObject();
        obj.put("reference-counts", eval.countReferenceOutcomes());
        obj.put("predicted-counts", eval.countPredictedOutcomes());
        obj.put("correct-counts", eval.countCorrectOutcomes());
        obj.put("precision", eval.precision());
        obj.put("recall", eval.recall());
        obj.put("f1", eval.f1());
        return obj.toJSONString();
    }


}
