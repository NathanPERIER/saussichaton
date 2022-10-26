
from threads import shared
from utils.sockets import Socket

import sys

def run(s: Socket) :
	while(True) :
		line = input()
		with shared.mutex :
			if shared.prompt is not None :
				print()
				res = shared.prompt.accept(line.strip())
				if res is not None :
					s.send(res)
				else :
					print('Invalid input, please try again > ', end='')
					sys.stdout.flush()
