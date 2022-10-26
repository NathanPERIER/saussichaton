
from threads import shared
from utils.sockets import Socket
from utils.terminal import flush_input

def run(s: Socket) :
	while(True) :
		line = input()
		with shared.mutex :
			if shared.prompt is not None :
				res = shared.prompt.accept(line.strip())
				if res is not None :
					s.send(res)
				else :
					flush_input()
					print('Invalid input, please try again > ', end='')
