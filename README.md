Event generator
===

The purpose of this project is to create an event generator that uses avro schema and set of predefined generators to generate a set of events.

_Note: this project exists solely for educational purposes_

## Overview

Event generator is a command-line tool that support the following features:
 - different types of output
    - stdout
    - file
 - using one or more avro schema by combining multiple generators
 - external generators
 - specifying how many events should be generated

## Usage scenario

`eventgen --count 5000 --include ~/generators --schema ~/schema/event-1.asvc --output stdout`

_event-1.asvc_
```json
{
  "type": "record",
  "name": "EmployeeCreated",
  "doc": "describes a scenario when an employee has been successfully created ",
  "fields": [
    { "name": "name", "type": "string", "doc": "employee name", "generator": "EmployeeNameGenerator" },
    { "name": "newcomerBonus", "type": ["null","double"], "doc": "describes a bonus that newcomer may get", "generator": "Range[Double](from = 0, to = 2000)"},
    {"name": "baseSalary", "type": "double", "doc": "the base salary of the employee", "generator": "Range[Double](from = 75000, to = 120000)" }
  ]
}
```
