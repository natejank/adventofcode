#!/usr/bin/env python3
import sys


def main(input_file: str):
    # parse input file
    packets = []
    with open(input_file) as handle:
        for line in handle:
            if line == '\n':
                pass
            else:
                packets.append(eval(line))

    print(f'Part 1: {part1(packets)}')
    print(f'Part 2: {part2(packets)}')


def part1(packets):
    output = 0
    group = 0
    for i in range(0, len(packets), 2):
        group += 1
        left = packets[i]
        right = packets[i + 1]
        result = compare(left, right)
        if result > 0:
            output += group
    return output


def part2(packets):
    sorted_packets = []
    divider_positions = []

    for packet in packets:
        for i in range(len(sorted_packets)):
            if compare(packet, sorted_packets[i]) > 0:
                sorted_packets.insert(i, packet)
                break
        else:
            sorted_packets.append(packet)
    for divider in [[[2]], [[6]]]:
        for i in range(len(sorted_packets)):
            if compare(divider, sorted_packets[i]) > 0:
                sorted_packets.insert(i, divider)
                divider_positions.append(i + 1)
                break

    return divider_positions[0] * divider_positions[1]


def compare(left, right):
    max_len = min(len(left), len(right))
    for i in range(max_len):
        left_val = left[i]
        right_val = right[i]
        if type(left_val) == int and type(right_val) == int:
            result = right_val - left_val
        elif type(left_val) == list and type(right_val) == list:
            result = compare(left_val, right_val)
        elif type(left_val) == list:
            result = compare(left_val, [right_val])
        else:
            result = compare([left_val], right_val)

        if result != 0:
            return result
    return len(right) - len(left)


if __name__ == '__main__':
    main(sys.argv[1])
