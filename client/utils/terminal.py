

try:
	# For Windows (maybe, not tested)
	import msvcrt
	def flush_input() :
		while msvcrt.kbhit() :
			msvcrt.getch()

except ImportError:
	import sys, termios
	def flush_input() :
		termios.tcflush(sys.stdin, termios.TCIOFLUSH)
