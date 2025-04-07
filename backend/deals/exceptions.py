# deals/exceptions.py
from rest_framework.exceptions import APIException
from rest_framework import status


class DealNotFoundError(APIException):
    status_code = status.HTTP_404_NOT_FOUND
    default_detail = 'Deal not found'
    default_code = 'not_found'


class ValidationError(APIException):
    status_code = status.HTTP_400_BAD_REQUEST
    default_detail = 'Invalid data'
    default_code = 'validation_error'
