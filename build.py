#!/usr/bin/env python3
import argparse
import sys
import subprocess
from pathlib import Path

BUILD_DIR = 'out'
COMPILE_FLAGS = ('-d', BUILD_DIR)
PACKAGE = Path('net', 'sloshy', 'aoc')


def find_java_files(source_root=Path('src')):
    paths = [source_root]
    files = []
    while len(paths) > 0:
        directory = paths.pop(0)
        for path in directory.iterdir():
            if path.is_dir() and path not in paths:
                paths.append(path)
        for file in directory.glob('*.java'):
            if file not in files:
                files.append(str(file))
    return files


def compile_java(*files: str):
    subprocess.run(['javac', *COMPILE_FLAGS, *files])


def get_input_path(year: str, day: str, test: bool) -> Path:
    return Path('lib', f'20{year}', f'day{day}{"_test" if test else ""}.input.txt')


def run_day(year: str, day: str, input_file: str):
    path = PACKAGE.joinpath(f'x{year}').joinpath(f'Day{day}')
    subprocess.run(['java', '-classpath', BUILD_DIR, str(path), input_file])


def main():
    argument_parser = argparse.ArgumentParser(description='An advent of code build system')
    argument_parser.add_argument('directive', help='Run directive.  Possible options are [run]')
    argument_parser.add_argument('day', help='Day to run')
    argument_parser.add_argument('year', help='Year to run. Valid formats are 4 digit (2022) or 2 digit (22)')
    argument_parser.add_argument('--test', '-t', dest='test', action='store_true', help='Use the test input?')
    argument_parser.add_argument('--input', '-i', dest='input_file', help='Custom input file')
    # TODO:
    # clean (no args) to rm -r out/*
    # compile (no args) to recursively compile the whole project

    args = argument_parser.parse_args()
    year = args.year
    day = args.day

    # truncate year to 2 digits
    if len(year) > 2:
        year = year[2:]

    # find input file
    if args.input_file is not None:
        input_file = args.input_file
    else:
        input_file = str(get_input_path(year, day, args.test))

    match args.directive:
        case 'run':
            compile_java(*find_java_files())
            run_day(year, day, input_file)
        case _:
            print('Invalid directive!')
            sys.exit(1)


if __name__ == '__main__':
    main()
