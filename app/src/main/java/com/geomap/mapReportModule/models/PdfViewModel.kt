package com.geomap.mapReportModule.models

data class PdfViewModel(
    val ResponseCode : String,
    val ResponseData : PDFViewResponseData,
    val ResponseMessage : String,
    val ResponseStatus : String
)

data class PDFViewResponseData(
    val pdfLink : String
)