# JSON Transformer API - Terminal Tests

## Setup
Assuming your Spring Boot app runs on `http://localhost:8080`

## 1. MINIFY Transformation Tests

### Test 1.1: Basic Minification
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=minify" \
  -H "Content-Type: application/json" \
  -d @examples/example_pretty.json
```
**Expected Result:** Single line JSON without formatting
```json
{"name":"John","age":30,"address":{"street":"Main St","city":"New York","zip":"10001"},"hobbies":["reading","swimming","coding"]}
```

### Test 1.2: Minify Already Minified (Should work fine)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=minify" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Same minified JSON

### Test 1.3: Minify Invalid JSON (Error case)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=minify" \
  -H "Content-Type: application/json" \
  -d '{"name":"John","age":30,'
```
**Expected Result:** Error message about invalid JSON

## 2. PRETTY Transformation Tests

### Test 2.1: Basic Pretty Printing
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=pretty" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Formatted JSON with indentation
```json
{
  "name" : "John",
  "age" : 30,
  "address" : {
    "street" : "Main St",
    "city" : "New York",
    "zip" : "10001"
  },
  "hobbies" : [ "reading", "swimming", "coding" ]
}
```

### Test 2.2: Pretty Print Already Pretty (Should work fine)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=pretty" \
  -H "Content-Type: application/json" \
  -d @examples/example_pretty.json
```
**Expected Result:** Same pretty-printed JSON

### Test 2.3: Pretty Print Complex Nested Structure
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=pretty" \
  -H "Content-Type: application/json" \
  -d '{"users":[{"name":"John","details":{"age":30,"skills":["java","python"]}},{"name":"Jane","details":{"age":25,"skills":["javascript","react"]}}]}'
```
**Expected Result:** Well-formatted nested structure

## 3. FILTER Transformation Tests

### Test 3.1: Basic Key Filtering
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter&keys=name,age" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Only name and age fields
```json
{"name":"John","age":30}
```

### Test 3.2: Filter with Nested Objects
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter&keys=name,address" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Name and entire address object (nested structure preserved)
```json
{"name":"John","address":{"street":"Main St","city":"New York","zip":"10001"}}
```

### Test 3.3: Filter Non-existent Keys (Should return empty object)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter&keys=nonexistent,alsomissing" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Empty object
```json
{}
```

## 4. COMBINATION Tests (Chaining Transformations)

### Test 4.1: Filter + Pretty Print
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter,pretty&keys=name,address" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Filtered and pretty-printed
```json
{
  "name" : "John",
  "address" : {
    "street" : "Main St",
    "city" : "New York",
    "zip" : "10001"
  }
}
```

### Test 4.2: Pretty + Minify (Should end up minified)
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=pretty,minify" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Back to minified format

### Test 4.3: Filter + Minify + Pretty
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter,minify,pretty&keys=name,hobbies" \
  -H "Content-Type: application/json" \
  -d @examples/example_pretty.json
```
**Expected Result:** Filtered, then pretty-printed
```json
{
  "name" : "John",
  "hobbies" : [ "reading", "swimming", "coding" ]
}
```

## 5. ERROR Cases

### Test 5.1: Filter Without Keys Parameter
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Error message about missing keys parameter

### Test 5.2: Empty Request Body
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=pretty" \
  -H "Content-Type: application/json"
```
**Expected Result:** Error message about missing JSON input

### Test 5.3: Invalid Transformation Type
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=unknown" \
  -H "Content-Type: application/json" \
  -d @examples/example_minified.json
```
**Expected Result:** Falls back to raw (no transformation)

## 6. UTILITY Endpoints

### Test 6.1: Get Available Options
```bash
curl -X GET "http://localhost:8080/json-tools/options"
```
**Expected Result:**
```json
["minify","pretty","filter","raw"]
```

## Advanced Test Cases

### Test A1: Complex Nested Filtering
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter&keys=name,address" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John",
    "age": 30,
    "email": "john@example.com",
    "address": {
      "street": "Main St",
      "city": "New York",
      "zip": "10001",
      "country": "USA"
    },
    "contacts": [
      {"type": "phone", "value": "123-456-7890"},
      {"type": "email", "value": "john@work.com"}
    ]
  }'
```

### Test A2: Array of Objects Filtering
```bash
curl -X POST "http://localhost:8080/json-tools/transform?type=filter,pretty&keys=name,age" \
  -H "Content-Type: application/json" \
  -d '[
    {"name": "John", "age": 30, "email": "john@example.com"},
    {"name": "Jane", "age": 25, "email": "jane@example.com"},
    {"name": "Bob", "age": 35, "email": "bob@example.com"}
  ]'
```