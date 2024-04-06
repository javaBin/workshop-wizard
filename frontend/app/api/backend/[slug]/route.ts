import { NextRequest } from "next/server";
import { handleAPI } from "@/lib/handleApi";

type Parameters = { params: { slug: string } };

export async function GET(request: NextRequest, context: Parameters) {
  return handleAPI(request, context);
}

export async function POST(request: NextRequest, context: Parameters) {
  return handleAPI(request, context);
}

export async function PUT(request: NextRequest, context: Parameters) {
  return handleAPI(request, context);
}

export async function DELETE(request: NextRequest, context: Parameters) {
  return handleAPI(request, context);
}
