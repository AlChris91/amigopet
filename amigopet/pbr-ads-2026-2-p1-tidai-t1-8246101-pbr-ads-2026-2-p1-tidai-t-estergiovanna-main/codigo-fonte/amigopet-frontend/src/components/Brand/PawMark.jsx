function PawMark({ className, label }) {
  return (
    <svg
      className={className}
      viewBox="0 0 160 140"
      role={label ? 'img' : undefined}
      aria-label={label}
      aria-hidden={label ? undefined : 'true'}
      xmlns="http://www.w3.org/2000/svg"
    >
      <circle cx="29" cy="52" r="17" fill="currentColor" />
      <circle cx="62" cy="22" r="17" fill="currentColor" />
      <circle cx="98" cy="22" r="17" fill="currentColor" />
      <circle cx="131" cy="52" r="17" fill="currentColor" />
      <path
        fill="currentColor"
        transform="translate(-0 -0) scale(1 1)"
        d="M75 51c-11 0-17 9-21 19-4 5-5 16-20 22-11 7-12 21-3 30 16 16 72 16 100 0 9-9 8-23-3-30-10-6-16-12-20-22-4-10-10-19-21-19Z"
      />
    </svg>
  );
}

export default PawMark;