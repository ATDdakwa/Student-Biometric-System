import React from "react";
import { saveAs } from "file-saver";
import { PDFDocument, StandardFonts, rgb } from "pdf-lib";
import { utils as XLSXUtils, writeFile as writeXLSXFile } from "xlsx";
import { IoMdArrowForward } from "react-icons/io";

const TableExport = ({ data, filename }) => {
  const handleExportExcel = () => {
    const workbook = XLSXUtils.book_new();
    const worksheet = XLSXUtils.json_to_sheet(data);
    XLSXUtils.book_append_sheet(workbook, worksheet, "Sheet1");
    const excelBuffer = XLSXUtils.write(workbook, { bookType: "xlsx", type: "array" });
    saveAs(new Blob([excelBuffer]), `${filename}.xlsx`);
  };

  const handleExportCSV = () => {
    const csvData = data.map((item) =>
      Object.values(item).map((value) => `"${value}"`).join(",")
    );
    const csvContent = csvData.join("\n");
    const csvBlob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    saveAs(csvBlob, `${filename}.csv`);
  };

  const handleExportPDF = async () => {
    const pdfDoc = await PDFDocument.create();
    const page = pdfDoc.addPage();
    const { width, height } = page.getSize();

    const font = await pdfDoc.embedFont(StandardFonts.Helvetica);

    const fontSize = 12;
    const margin = 50;
    const lineHeight = fontSize + 5;
    const textOptions = {
      font: font,
      size: fontSize,
      lineHeight: lineHeight,
      color: rgb(0, 0, 0),
    };

    let y = height - margin;

    data.forEach((item) => {
      const row = Object.values(item).join(" | ");
      page.drawText(row, { x: margin, y: y, ...textOptions });
      y -= lineHeight;
    });

    const pdfBytes = await pdfDoc.save();
    saveAs(new Blob([pdfBytes], { type: "application/pdf" }), `${filename}.pdf`);
  };

  return (
    <div>
      {/* <button
        onClick={handleExportExcel}
        className="px-2 py-1 text-sm bg-gray-300 rounded-lg mr-2"
      >
        Excel
      </button> */}
      <button
        onClick={handleExportCSV}
        style={{backgroundColor:'#000630'}}
        className="px-2 py-1 text-sm  text-white rounded-lg flex items-center space-x-1"
      >
        <span>Export To Excel</span>
        <IoMdArrowForward className="w-4 h-4" />
      </button>
      {/* <button
        onClick={handleExportPDF}
        className="px-2 py-1 text-sm bg-gray-300 rounded-lg"
      >
        PDF
      </button> */}
    </div>
  );
};

export default TableExport;