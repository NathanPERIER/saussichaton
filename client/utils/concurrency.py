
from threading import Lock


class MutexLock :

	def __init__(self) :
		self.mutex = Lock()
	
	def __enter__(self) -> "MutexLock" :
		self.mutex.acquire()
		return self
	
	def __exit__(self) :
		self.mutex.release()
