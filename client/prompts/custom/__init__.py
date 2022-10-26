
from prompts.base import Prompt
from prompts.custom.integer import IntPrompt
from prompts.custom.string import StringPrompt
from prompts.custom.list import ListPrompt
from prompts.custom.multilist import MultiListPrompt

from typing import Mapping, Type


prompt_types: "Mapping[str,Type[Prompt]]" = {
	'prompt_integer': IntPrompt,
	'prompt_string': StringPrompt,
	'prompt_list': ListPrompt,
	'prompt_list_multi': MultiListPrompt
}
