package com.elvo.service.impl;

import com.elvo.entity.Order;
import com.elvo.entity.OrderItems;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class EBillGenerator {

    public static String generateBill(Order order) {
        String directoryPath = "/ebills";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir(); // Create the directory if it doesn't exist
        }

        String pdfPath = directoryPath + "/order_" + order.getId() + ".pdf"; // Use / for cross-platform compatibility

        try {
            // Create a PDF writer
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfPath));
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Title
            Paragraph title = new Paragraph("Clothify Store - Panadura")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(18);
            document.add(title);

            Paragraph sub = new Paragraph("E-Bill - Order #" + order.getId())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(10);
            document.add(sub);

            // Add Order Details
            Paragraph orderDetails = new Paragraph("Order Date: " + order.getOrderDate() +
                    "\nCustomer Email: " + order.getCustomerEmail() +
                    "\nTotal Amount: Rs." + String.format("%.2f", order.getTotalAmount()) +
                    "\n\n");
            orderDetails.setTextAlignment(TextAlignment.LEFT);
            document.add(orderDetails);

            // Create a table for Order Items
            float[] columnWidths = {100f, 100f, 100f, 100f};
            Table table = new Table(columnWidths);
            table.addCell(new Cell().add(new Paragraph("Item ID").setBold()));
            table.addCell(new Cell().add(new Paragraph("Quantity").setBold()));
            table.addCell(new Cell().add(new Paragraph("Unit Price").setBold()));
            table.addCell(new Cell().add(new Paragraph("Total Price").setBold()));

            List<OrderItems> orderItems = order.getOrderItems();
            for (OrderItems item : orderItems) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getItemId()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", item.getUnitPrice()))));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", item.getTotalPrice()))));
            }

            document.add(table);

            Paragraph bottom = new Paragraph("Thank you... Come again...")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(8);
            document.add(bottom);
            document.close();

            return pdfPath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
