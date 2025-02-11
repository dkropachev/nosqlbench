/*
 *
 *    Copyright 2016 jshook
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * /
 */

package io.nosqlbench.engine.api.activityconfig.rawyaml;

import io.nosqlbench.nb.api.errors.BasicError;

import java.util.*;

public class RawStmtDef extends RawStmtFields {

    private Object op;

    private final static List<String> opNames = List.of("stmt","statement","op","operation");

    public RawStmtDef() {
    }

    public RawStmtDef(String name, String op) {
        setName(name);
        this.op = op;
    }

    @SuppressWarnings("unchecked")
    public RawStmtDef(String defaultName, Map<String, Object> map) {
        HashSet<String> found = new HashSet<>();
        for (String opName : opNames) {
            if (map.containsKey(opName)) {
                found.add(opName);
            }
        }
        if (found.size()>1) {
            throw new BasicError("You used " + found + " as an op name, but only one of these is allowed.");
        }
        if (found.size()==1) {
            Object op = map.remove(found.iterator().next());
            this.setOp(op);
        }

        Optional.ofNullable((String) map.remove("name")).ifPresent(this::setName);
        Optional.ofNullable((String) map.remove("desc")).ifPresent(this::setDesc);
        Optional.ofNullable((String) map.remove("description")).ifPresent(this::setDesc);

        Optional.ofNullable((Map<String, String>) map.remove("tags")).ifPresent(this::setTags);
        Optional.ofNullable((Map<String, String>) map.remove("bindings")).ifPresent(this::setBindings);
        Optional.ofNullable((Map<String, Object>) map.remove("params")).ifPresent(this::setParams);


        // Depends on order stability, relying on LinkedHashMap -- Needs stability unit tests
        if (this.op == null) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            if (!iterator.hasNext()) {
                throw new RuntimeException("undefined-name-statement-tuple:" +
                        " The statement is not set, and no statements remain to pull 'name: statement' values from." +
                        " For more details on this error see " +
                        "the troubleshooting section of the YAML format" +
                        " docs for undefined-name-statement-tuple");
            }
            Map.Entry<String, Object> firstEntry = iterator.next();
            if (firstEntry.getValue() instanceof Map && map.size()==1) {
                Map values = (Map) firstEntry.getValue();
                setFieldsByReflection(values);
                map = values;
            } else if (firstEntry.getValue() instanceof CharSequence) {
                setStmt(((CharSequence) firstEntry.getValue()).toString());
            }
            if (getName().isEmpty()) {
                map.remove(firstEntry.getKey());
                setName(firstEntry.getKey());
            }
            // TODO: Add explicit check condition for this error
//            else {
//                throw new RuntimeException("redefined-name-in-statement-tuple: Statement name has already been set by name parameter. Remove the name parameter for a statement definition map." +
//                        " For more details on this error see " +
//                        "the troubleshooting section in the " +
//                        "YAML format docs for redefined-name-statement-tuple");
//            }
        }
        if (getName().isEmpty()) {
            setName(defaultName);
        }

        map.forEach((key, value) -> getParams().put(key, value));
    }

    private void setOp(Object op) {
        this.op = op;
    }

    public String getStmt() {
        if (op instanceof CharSequence) {
            return op.toString();
        } else {
            throw new BasicError("tried to access a non-char statement definition with #getStmt()");
        }
    }

    public Object getOp() {
        return op;
    }

    private void setStmt(String statement) {
        this.op = statement;
    }

    public String getName() {
        Object name = getParams().get("name");
        if (name!=null) {
            return name.toString();
        }
        return super.getName();
    }
}
