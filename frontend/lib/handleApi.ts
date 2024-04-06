import { NextRequest, NextResponse } from "next/server";
import { getAccessToken, withApiAuthRequired } from "@auth0/nextjs-auth0";

const API_URL = process.env.API_URL;
export const handleAPI = withApiAuthRequired(async function handleAPI(
  req: NextRequest,
) {
  // If your access token is expired and you have a refresh token
  // `getAccessToken` will fetch you a new one using the `refresh_token` grant
  const { accessToken } = await getAccessToken();
  const { path, body, method } = await getPathAndBody(req);

  const response = await fetch(`${API_URL}/${path}`, {
    method: method,
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
    body: body,
  });

  return handleResponse(response);
});

const getPathAndBody = async (req: NextRequest) => {
  const path = req.nextUrl.pathname.replace("/api/backend/", "");
  const body = req.body ? JSON.stringify(await req.json()) : undefined;
  const method = req.method;
  return { path, body, method };
};

const handleResponse = async (res: Response) => {
  if (res.status > 199 && res.status < 300) {
    if (res.body) {
      return NextResponse.json(await res.json());
    } else {
      return new NextResponse(res.statusText, { status: res.status });
    }
  } else {
    return new NextResponse(res.statusText, { status: res.status });
  }
};
