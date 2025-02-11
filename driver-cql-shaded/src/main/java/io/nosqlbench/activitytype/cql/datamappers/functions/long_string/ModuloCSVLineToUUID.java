/*
 *

 *       Copyright 2015 Jonathan Shook
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package io.nosqlbench.activitytype.cql.datamappers.functions.long_string;

import io.nosqlbench.nb.api.content.NBIO;
import io.nosqlbench.virtdata.api.annotations.Categories;
import io.nosqlbench.virtdata.api.annotations.Category;
import io.nosqlbench.virtdata.api.annotations.Example;
import io.nosqlbench.virtdata.api.annotations.ThreadSafeMapper;
import io.nosqlbench.virtdata.library.basics.shared.from_long.to_string.ModuloLineToString;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.LongFunction;

/**
 * Select a value from a CSV file line by modulo division against the number
 * of lines in the file. The second parameter is the field name, and this must
 * be provided in the CSV header line as written. This version of the function is
 * a type-safe getter for UUID values.
 * @deprecated Use ModuloCSVLineToString(); ToUUID() instead
 */
@ThreadSafeMapper
@Categories({Category.general})
public class ModuloCSVLineToUUID implements LongFunction<UUID> {
    private final static Logger logger = LogManager.getLogger(ModuloLineToString.class);

    private final List<String> lines = new ArrayList<>();

    private final String filename;

    @Example({"ModuloCSVLineToUUID('data/myfile.csv','lat')","load values for 'lat' from the CSV file myfile.csv."})
    public ModuloCSVLineToUUID(String filename, String fieldname) {
        this.filename = filename;
        CSVParser csvp = NBIO.readFileCSV(filename);
        int column = csvp.getHeaderMap().get(fieldname);
        for (CSVRecord strings : csvp) {
            if (strings.get(column) != null) {
                lines.add(strings.get(column));
            }
        }
    }

    @Override
    public UUID apply(long input) {
        int itemIdx = (int) (input % lines.size()) % Integer.MAX_VALUE;
        String item = lines.get(itemIdx);
        return UUID.fromString(item);
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + filename;
    }


}
