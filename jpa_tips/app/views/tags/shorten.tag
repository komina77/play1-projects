#{set '_doBody'}#{doBody/}#{/set}
%{
    if (!_str) {
        _str = _doBody;
    }
    if (!_length || _length == 0) {
        throw new play.exceptions.TagInternalException("length attribute cannot be empty for shorten tag");
    }
}%
%{
    if (_str.length() > _length) {
}%
${_str[0.._length]}${_cont ? _cont : '...'}
%{
    } else {
}%
${_str}
%{
    }
}%