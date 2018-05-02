#!/usr/bin/env python3

import random
import sys

random.seed(int(sys.argv[1]))
K = random.randrange(2500)
S = random.randrange(3 * K + 1)

print("{} {}".format(K, S))

